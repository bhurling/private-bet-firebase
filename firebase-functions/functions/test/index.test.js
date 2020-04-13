const sinon = require('sinon')
const test = require('firebase-functions-test')()

// Require firebase-admin so we can stub out some of its methods.
const admin = require('firebase-admin')

// Require dependencies so we can stub out their methods.
const authentication = require('../wrappers/authentication.js')
const database = require('../wrappers/database.js')
const messaging = require('../wrappers/messaging.js')

const myFunctions = require('../index')

describe('onInvitationSent', () => {
    beforeEach (() => {
        databaseFetchStub = sinon.stub(database, 'fetchOnce')
        databaseCreateStub = sinon.stub(database, 'create')
        databaseUpdateStub = sinon.stub(database, 'update')
        authStub = sinon.stub(authentication, 'getUser')
    })

    afterEach (() => {
        databaseFetchStub.restore()
        databaseCreateStub.restore()
        databaseUpdateStub.restore()
        authStub.restore()
    })

    describe('when there is no incoming link for UserB yet', () => {
        beforeEach (() => {
            databaseFetchStub.withArgs('/links/UserB/incoming/UserA').returns(test.firestore.makeDocumentSnapshot())
            authStub.withArgs('UserA').returns({ uid: 'user_a', photoURL: 'url_a', displayName: 'User A' })
            authStub.withArgs('UserB').returns({ uid: 'user_b', photoURL: 'url_b', displayName: 'User B' })
        })

        describe('when UserA sends invitation to UserB', () => {
            const snap = test.firestore.makeDocumentSnapshot()
            const context = { params: { receiverUid: "UserB", senderUid: "UserA" } }
    
            it('would create the incoming link for UserB', () => {
                return test.wrap(myFunctions.onInvitationSent)(snap, context).then(() => {
                    sinon.assert.calledWith(
                        databaseCreateStub,
                        "/links/UserB/incoming/UserA",
                        sinon.match.any
                    )
                })
            })
        })
    })

    describe('when there is an incoming link for UserB already', () => {
        beforeEach (() => {
            databaseFetchStub.withArgs('/links/UserB/incoming/UserA').returns(test.firestore.makeDocumentSnapshot( { linked: true }))
        })

        describe('when UserA sends invitation to UserB', () => {
            const snap = test.firestore.makeDocumentSnapshot()
            const context = { params: { receiverUid: "UserB", senderUid: "UserA" } }
    
            it('would not create another incoming link for UserB', () => {
                return test.wrap(myFunctions.onInvitationSent)(snap, context).then(() => {
                    sinon.assert.neverCalledWith(
                        databaseCreateStub,
                        "/links/UserB/incoming/UserA",
                        sinon.match.any
                    )
                })
            })
        })
    })

})

describe('onInvitationConfirmed', () => {

    beforeEach(() => {
        databaseFetchStub = sinon.stub(database, 'fetchOnce')
        databaseCreateStub = sinon.stub(database, 'create')
        databaseUpdateStub = sinon.stub(database, 'update')
        authStub = sinon.stub(authentication, 'getUser')
    })

    afterEach(() => {
        databaseFetchStub.restore()
        databaseCreateStub.restore()
        databaseUpdateStub.restore()
        authStub.restore()
    })

    describe('when UserA confirms invitation from UserB', () => {
        beforeEach(() => {
            databaseFetchStub.withArgs('/links/UserB/confirmed/UserA').returns(test.firestore.makeDocumentSnapshot())
            authStub.withArgs('UserA').returns({ uid: 'user_a', photoURL: 'url_a', displayName: 'User A' })
            authStub.withArgs('UserB').returns({ uid: 'user_b', photoURL: 'url_b', displayName: 'User B' })
        })

        it('would create confirmed link for UserB', () => {
            const snap = test.firestore.makeDocumentSnapshot()
            const context = { params: { receiverUid: "UserA", senderUid: "UserB" } }

            return test.wrap(myFunctions.onInvitationConfirmed)(snap, context).then(() => {
                sinon.assert.calledWith(
                    databaseCreateStub,
                    "/links/UserB/confirmed/UserA",
                    sinon.match.any
                )
            })
        })
    })
})

describe('onInvitationReceived', () => {
    beforeEach (() => {
        snap = test.firestore.makeDocumentSnapshot()

        databaseFetchStub = sinon.stub(database, 'fetchOnce')
        authStub = sinon.stub(authentication, 'getUser')
        messagingStub = sinon.stub(messaging, 'sendToDevice')    
    })

    afterEach (() => {
        databaseFetchStub.restore()
        authStub.restore()
        messagingStub.restore()
    })

    describe('when UserB receives invitation from UserA', () => {

        beforeEach(() => {
            context = { params: { receiverUid: "UserB", senderUid: "UserA" } }
        })

        describe('when UserB has a device token registered', () => {

            beforeEach(() => {
                deviceSnap = test.firestore.makeDocumentSnapshot({
                    device_0: true
                }, '/devices/UserB')
    
                databaseFetchStub.withArgs('/devices/UserB').returns(deviceSnap)
            })

            it('would send message with key "InvitationNew" to UserB.', () => {
                authStub.withArgs('UserA').returns({uid: "user_a"})
    
                expectedTokens = ['device_0']
                expectedPayload = sinon.match.hasNested('data.key', 'InvitationNew')
                    .and(sinon.match.hasNested('data.custom', '{"senderId":"user_a"}'))

                return test.wrap(myFunctions.onInvitationReceived)(snap, context).then(() => {
                    sinon.assert.calledWith(
                        messagingStub,
                        expectedTokens,
                        expectedPayload
                    )
                })
            })
        })
    })
})