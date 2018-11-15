const test = require('firebase-functions-test')()
const sinon = require('sinon')

// Require firebase-admin so we can stub out some of its methods.
const admin = require('firebase-admin')

describe('sendNotificationToInvitee', () => {
    let myFunctions, adminInitStub

    before(() => {
        adminInitStub = sinon.stub(admin, 'initializeApp')
        myFunctions = require('../index')
    })

    after(() => {
        adminInitStub.restore()
    })

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
                const refParams = '/links/UserA/incoming/UserB'
                const databaseStub = sinon.stub()
                const refStub = sinon.stub()
                const onceStub = sinon.stub()

                // Stub calls for admin.database().ref(...)
                Object.defineProperty(admin, 'database', { get: () => databaseStub })
                databaseStub.returns({ ref: refStub })
                refStub.withArgs(refParams).returns({once: onceStub})

                // Stub call for once('value') so it returns a snapshot with val() = true
                onceStub.withArgs('value').returns({val: () => true})

                return test.wrap(myFunctions.sendNotificationToInvitee)(change, context).then(() => {
                    // TODO assert that stuff has been called
                })
            })
        })
    })
})