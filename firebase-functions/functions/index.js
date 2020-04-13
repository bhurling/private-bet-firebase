const functions = require('firebase-functions');
const admin = require('firebase-admin');

const authentication = require('./wrappers/authentication.js')
const database = require('./wrappers/database.js')
const messaging = require('./wrappers/messaging.js')

admin.initializeApp();

exports.onInvitationSent = functions.firestore.document('/links/{senderUid}/outgoing/{receiverUid}')
    .onCreate(async (snapshot, context) => {
        const receiverUid = context.params.receiverUid
        const senderUid = context.params.senderUid

        console.log(`${senderUid} wants to invite ${receiverUid}`)

        const receiverLink = await database.fetchOnce(`/links/${receiverUid}/incoming/${senderUid}`)

        if (receiverLink.data()) {
            console.log(`Incoming link for ${receiverUid} already exists`)

            return null
        } else {
            console.log(`Creating incoming link for ${receiverUid}`)

            return database.create(`/links/${receiverUid}/incoming/${senderUid}`, { linked: true } )
        }
})

exports.onInvitationRevoked = functions.firestore.document('/links/{senderUid}/outgoing/{receiverUid}')
    .onDelete(async (snapshot, context) => {
        const receiverUid = context.params.receiverUid
        const senderUid = context.params.senderUid

        console.log(`${senderUid} wants to revoke invitation for ${receiverUid}`)

        const receiverLink = await admin.firestore().doc(`/links/${receiverUid}/incoming/${senderUid}`).get()

        if (receiverLink.data()) {
            console.log(`Deleting incoming link for ${receiverUid}`)

            return admin.firestore().doc(`/links/${receiverUid}/incoming/${senderUid}`).delete()
        } else {
            console.log(`Incoming link for ${receiverUid} did not exist`)

            return null
        }
})

exports.onInvitationDeclined = functions.firestore.document('/links/{receiverUid}/incoming/{senderUid}')
    .onDelete(async (snapshot, context) => {
        const receiverUid = context.params.receiverUid
        const senderUid = context.params.senderUid

        console.log(`${receiverUid} wants to decline invitation from ${senderUid}`)

        const senderLink = await admin.firestore().doc(`/links/${senderUid}/outgoing/${receiverUid}`).get()

        if (senderLink.data()) {
            console.log(`Deleting outgoing link from ${senderUid}`)

            return admin.firestore().doc(`/links/${senderUid}/outgoing/${receiverUid}`).delete()
        } else {
            console.log(`Outgoing link from ${senderUid} did not exist`)

            return null
        }
})

exports.onInvitationConfirmed = functions.firestore.document('/links/{receiverUid}/confirmed/{senderUid}')
    .onCreate(async (snapshot, context) => {
        const receiverUid = context.params.receiverUid
        const senderUid = context.params.senderUid

        console.log(`${receiverUid} wants to confirm invitation from ${senderUid}`)

        const senderLink = await database.fetchOnce(`/links/${senderUid}/confirmed/${receiverUid}`)

        if (senderLink.data()) {
            console.log(`Confirmed link for ${senderUid} already exists`)

            return null
        } else {
            console.log(`Creating confirmed link for ${senderUid}`)

            return database.create(`/links/${senderUid}/confirmed/${receiverUid}`, { linked: true } )
        }
})

exports.onInvitationReceived = functions.firestore.document('/links/{receiverUid}/incoming/{senderUid}')
    .onCreate(async (snapshot, context) => {
        const receiverUid = context.params.receiverUid
        const senderUid = context.params.senderUid

        console.log(`Incoming invitation for ${receiverUid} from ${senderUid}`)

        const getDeviceTokensPromise = database.fetchOnce(`/devices/${receiverUid}`)
        const getUserProfilePromise = authentication.getUser(senderUid)

        const results = await Promise.all([getDeviceTokensPromise, getUserProfilePromise])

        const tokens = Object.keys(results[0].data())
        const profile = results[1]

        if (tokens.length == 0) {
            console.log('There are no notification tokens to send to')

            return null
        }

        console.log(`There are ${tokens.length} tokens to send notifications to`)
        console.log(`Sender profile is ${profile}`)

        const payload = {
            data: {
                key: 'InvitationNew',
                id: profile.uid,
                custom: JSON.stringify({
                    senderImage: profile.photoURL,
                    senderId: profile.uid,
                    senderDisplayName: profile.displayName
                })
            }
        }

        return messaging.sendToDevice(tokens, payload)
})
