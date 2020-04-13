const admin = require('firebase-admin')

module.exports.fetchOnce = async (doc) => {
    return admin.firestore().doc(doc).get()
}

module.exports.create = async (doc, data) => {
    return admin.firestore().doc(doc).create(data)
}

module.exports.update = async (doc, data) => {
    return admin.firestore().doc(doc).update(data)
}