const admin = require('firebase-admin')

module.exports.fetchOnce = (ref) => {
    return admin.database().ref(ref).once('value')
}