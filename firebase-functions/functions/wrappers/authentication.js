const admin = require('firebase-admin')

module.exports.getUser = (uid) => {
    return admin.auth().getUser(uid)
}