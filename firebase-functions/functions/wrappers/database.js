const admin = require('firebase-admin')

module.exports.fetchOnce = (doc) => {
    return admin.firestore().doc(doc).get()
}