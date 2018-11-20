const sinon = require('sinon')
const test = require('firebase-functions-test')()

// Require firebase-admin so we can stub out some of its methods.
const admin = require('firebase-admin')

// Require dependencies so we can stub out their methods.
const database = require('../wrappers/database.js')
const authentication = require('../wrappers/authentication.js')

const myFunctions = require('../index')

describe('sendNotificationToInvitee', () => {

    describe('Given there is no invitation from UserA to UserB', () => {
        let beforeSnap, afterSnap, change, context

        before(() => {
            beforeSnap = {
                val: () => false
            }
        })

        describe('when UserA sends invitation to UserB', () => {
            before (() => {
                afterSnap = {
                    val: () => true
                }
                change = { before: beforeSnap, after: afterSnap }
                context = { params: { receiverUid: "UserB", senderUid: "UserA" } }
            })

            it('does not throw an error', () => {
                const snap = test.database.makeDataSnapshot({}, '/devices/UserB')

                const databaseStub = sinon.stub(database, 'fetchOnce')
                const authStub = sinon.stub(authentication, 'getUser')

                databaseStub.withArgs('/links/UserA/incoming/UserB').returns({val: () => null})
                databaseStub.withArgs('/devices/UserB').returns(snap)
                authStub.withArgs('UserA').returns({})

                return test.wrap(myFunctions.sendNotificationToInvitee)(change, context).then(() => {
                    // TODO assert that stuff has been called
                })
            })
        })
    })
})