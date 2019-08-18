const sinon = require('sinon')
const test = require('firebase-functions-test')()

// Require firebase-admin so we can stub out some of its methods.
const admin = require('firebase-admin')

// Require dependencies so we can stub out their methods.
const authentication = require('../wrappers/authentication.js')
const database = require('../wrappers/database.js')
const messaging = require('../wrappers/messaging.js')

const myFunctions = require('../index')

describe('sendNotificationToInvitee', () => {

    describe('Given there is no invitation from UserA to UserB', () => {
        let beforeSnap, afterSnap, change, context

        before(() => {
            beforeSnap = {
                data: () => false
            }
        })

        describe('when UserA sends invitation to UserB', () => {
            before (() => {
                afterSnap = {
                    data: () => true
                }
                change = { before: beforeSnap, after: afterSnap }
                context = { params: { receiverUid: "UserB", senderUid: "UserA" } }
            })

            it('sends InvitationNew message to UserB.', () => {
                const snap = test.firestore.makeDocumentSnapshot({
                    device_0: true
                }, '/devices/UserB')

                const databaseStub = sinon.stub(database, 'fetchOnce')
                const authStub = sinon.stub(authentication, 'getUser')
                const messagingStub = sinon.stub(messaging, 'sendToDevice')

                databaseStub.withArgs('/links/UserA/incoming/UserB').returns({data: () => null})
                databaseStub.withArgs('/devices/UserB').returns(snap)

                authStub.withArgs('UserA').returns({uid: "user_a"})

                return test.wrap(myFunctions.sendNotificationToInvitee)(change, context).then(() => {
                    sinon.assert.calledWith(
                        messagingStub,
                        ['device_0'],
                        sinon.match.has('data', sinon.match.has('key', 'InvitationNew'))
                    )
                })
            })
        })
    })
})