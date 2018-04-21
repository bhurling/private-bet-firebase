const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();

exports.sendNotificationToInvitee = functions.database.ref('/links/{receiverUid}/incoming/{senderUid}')
    .onWrite((change, context) => {
        const receiverUid = context.params.receiverUid;
        const senderUid = context.params.senderUid;

        console.log(`Incoming change for /links/${receiverUid}/incoming/${senderUid}`);

        if (!change.after.val()) {
            console.log('Invitation for', receiverUid, 'from', senderUid, 'has been removed');

            return 0;
        }

        console.log(`${senderUid} wants to invite ${receiverUid}`);
        console.log(`Checking other link /links/${senderUid}/incoming/${receiverUid}`)

        const otherLinkPromise = admin.database()
                .ref(`/links/${senderUid}/incoming/${receiverUid}`).once('value');

        return Promise.all([otherLinkPromise]).then(results => {
            const linkSnapshot = results[0]

            if (linkSnapshot.val() == true) {
                console.log('Other link exists, skip notification');

                return 0;
            }

            const getDeviceTokensPromise = admin.database()
                    .ref(`/devices/${receiverUid}`).once('value');

            const getUserProfilePromise = admin.auth().getUser(senderUid);

            return Promise.all([getDeviceTokensPromise, getUserProfilePromise]).then(results => {
                const tokensSnapshot = results[0]
                const profileSnapshot = results[1]

                if (tokensSnapshot.numChildren() == 0) {
                    console.log('There are no notification tokens to send to');

                    return 0;
                }

                console.log('There are', tokensSnapshot.numChildren(), 'tokens to send notifications to');
                console.log('Sender profile is', profileSnapshot)

                const payload = {
                    notification: {
                        title: 'You have a new invitation',
                        body: `${profileSnapshot.displayName} wants to challenge you on Private Bet.`,
                        icon: profileSnapshot.photoURL
                    }
                };

                const tokens = Object.keys(tokensSnapshot.val());

                return admin.messaging().sendToDevice(tokens, payload).then((response) => {
                    console.log(response)

                    return 0;
                });
            })
        })
});
