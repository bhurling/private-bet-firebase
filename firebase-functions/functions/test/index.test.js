const test = require('firebase-functions-test')()
const admin = require('firebase-admin')
const sinon = require('sinon')

const adminInitStub = sinon.stub(admin, 'initializeApp')
const myFunctions = require('../index.js')
const wrapped = test.wrap(myFunctions.sendNotificationToInvitee)

const refStub = sinon.stub()

describe('Given there is an existing invitation', function() {
    let databaseStub, context, beforeSnap

    before(function() {
        context = { params: { receiverUid: "abc", senderUid: "def"} }

        beforeSnap = {
            val: () => true
        }
    })

    describe('when a user removes invitation', function() {

        let afterSnap, change

        before(function() {
            afterSnap = {
                val: () => false
            }
            change = { before: beforeSnap, after: afterSnap }
        })

        it('will not break', function() {
            return wrapped(change, context)
        })
    })
})