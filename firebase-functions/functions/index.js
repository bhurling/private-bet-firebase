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

        const receiverLink = await admin.firestore().doc(`/links/${receiverUid}/incoming/${senderUid}`).get()

        if (receiverLink.data()) {
            console.log(`Incoming link for ${receiverUid} already exists`)

            return null
        } else {
            console.log(`Creating incoming link for ${receiverUid}`)

            return admin.firestore().doc(`/links/${receiverUid}/incoming/${senderUid}`).create( { linked: true } )
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

        console.log(`${receiverUid} wants to declined invitation from ${senderUid}`)

        const senderLink = await admin.firestore().doc(`/links/${senderUid}/outgoing/${receiverUid}`).get()

        if (senderLink.data()) {
            console.log(`Deleting outgoing link from ${senderUid}`)

            return admin.firestore().doc(`/links/${senderUid}/outgoing/${receiverUid}`).delete()
        } else {
            console.log(`Outgoing link fron ${senderUid} did not exist`)

            return null
        }
})

exports.onInvitationConfirmed = functions.firestore.document('/links/{receiverUid}/confirmed/{senderUid}')
    .onCreate(async (snapshot, context) => {
        const receiverUid = context.params.receiverUid
        const senderUid = context.params.senderUid

        console.log(`${receiverUid} wants to confirm invitation from ${senderUid}`)

        const senderLink = await admin.firestore().doc(`/links/${senderUid}/confirmed/${receiverUid}`).get()

        if (senderLink.data()) {
            console.log(`Confirmed link for ${senderUid} already exists`)

            return null
        } else {
            console.log(`Creating confirmed link for ${senderUid}`)

            return admin.firestore().doc(`/links/${senderUid}/confirmed/${receiverUid}`).create( { linked: true } )
        }
})

exports.sendNotificationToInvitee = functions.firestore.document('/links/{receiverUid}/incoming/{senderUid}')
    .onWrite((change, context) => {
        const receiverUid = context.params.receiverUid;
        const senderUid = context.params.senderUid;

        console.log(`Incoming change for /links/${receiverUid}/incoming/${senderUid}`);

        if (!change.after.data()) {
            console.log('Invitation for', receiverUid, 'from', senderUid, 'has been removed');

            return 0;
        }

        console.log(`${senderUid} wants to invite ${receiverUid}`);
        console.log(`Checking other link /links/${senderUid}/incoming/${receiverUid}`)

        const otherLinkPromise = database.fetchOnce(`/links/${senderUid}/incoming/${receiverUid}`)

        return Promise.all([otherLinkPromise]).then(results => {
            const linkSnapshot = results[0]

            if (linkSnapshot.exists) {
                console.log('Other link exists, skip notification');

                return 0;
            }

            const getDeviceTokensPromise = database.fetchOnce(`/devices/${receiverUid}`)
            const getUserProfilePromise = authentication.getUser(senderUid);

            return Promise.all([getDeviceTokensPromise, getUserProfilePromise]).then(results => {
                const tokens = Object.keys(results[0].data());
                const profile = results[1];

                if (tokens.length == 0) {
                    console.log('There are no notification tokens to send to');

                    return 0;
                }

                console.log('There are', tokens.length, 'tokens to send notifications to');
                console.log('Sender profile is', profile)

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
                };

                const messagingPromise = messaging.sendToDevice(tokens, payload)

                return Promise.all([messagingPromise]).then((response) => {
                    console.log(response)

                    return 0;
                });
            })
        })
});
