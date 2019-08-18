const admin = require('firebase-admin')

module.exports.sendToDevice = (tokens, payload) => {
    return admin.messaging().sendToDevice(tokens, payload)
}