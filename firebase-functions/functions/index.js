const functions = require('firebase-functions');
const admin = require('firebase-admin');

const database = require('./wrappers/database.js')
const authentication = require('./wrappers/authentication.js')

admin.initializeApp();

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

            if (linkSnapshot.data() == true) {
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

                return admin.messaging().sendToDevice(tokens, payload).then((response) => {
                    console.log(response)

                    return 0;
                });
            })
        })
});
