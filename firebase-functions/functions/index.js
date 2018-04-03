const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();

exports.sendNotificationToInvitee = functions.database.ref('/links/{receiverUid}/incoming/{senderUid}')
    .onWrite((change, context) => {
        console.log(change)
        console.log(context)

        const receiverUid = context.params.receiverUid;
        const senderUid = context.params.senderUid;

        if (!change.after.val()) {
            return console.log('Invitation for ', receiverUid, ' from ', senderUid, ' has been removed.');
        }

        return console.log(senderUid, 'wants to invite ', receiverUid, '.');
});
