const admin = require("firebase-admin");
const functions = require("firebase-functions");
const serviceAccount = require("./key.json");
admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
  databaseURL: "https://go4lunch-3f2a8.firebaseio.com",
});

exports.sendNotifications = functions.pubsub.schedule("0 12 * * *")
    .timeZone("Europe/Paris").onRun((context) => {
      const db = admin.firestore();
      functions.logger.log("debug :", Date().valueOf());
      db.collection("users").get().then((snapshot) => {
        functions.logger.log("debug 2 :", Date().valueOf());
        for (let i = 0; i < snapshot.size; i++) {
          const currentId = snapshot.docs[i].data()["uid"];
          const idPlace = snapshot.docs[i].data()["idPlace"];
          const fcmToken = snapshot.docs[i].data()["fcmToken"];
          if (idPlace != null && idPlace != "") {
            notifyUser(db, currentId, fcmToken, idPlace);
          }
        }
        return null;
      }).catch((err) => {
        functions.logger.log("Error getting document", err);
      });
      return null;
    });

/**
 * @param {int} db Database.
 * @param {String} currentId id user
 * @param {String} fcmToken Firebase Messaging token for a user.
 * @param {String} idPlace Id Place for a user.
 */
function notifyUser(db, currentId, fcmToken, idPlace) {
  db.collection("participation").where("idPlace", "==", idPlace)
      .get().then((snapshot) => {
        functions.logger.log("debug 4 :", Date().valueOf());
        if (!snapshot.empty) {
          const address = snapshot.docs[0].data()["addressPlace"];
          const name = snapshot.docs[0].data()["namePlace"];
          const uids = snapshot.docs[0].data()["uid"];
          for (let i = 0; i < uids.length; i++) {
            if (uids[i] === currentId) {
              uids.splice(i, 1);
            }
          }
          console.log(uids);
          const participant = [];
          uids.forEach((uid) => db.collection("users").where("uid", "==", uid)
              .get().then((otherSnapshot) => {
                if (!otherSnapshot.empty) {
                  participant.push(otherSnapshot.docs[0].data()["displayName"]);
                  if (participant.length != uids.length) {
                    return;
                  }
                }
                console.log("TEST: " + participant);
                const message = participant.join(", ");
                functions.logger.log("debug 5 :", Date().valueOf());
                if (!snapshot.empty) {
                  const payload = {
                    notification: {
                      data_type: "direct_message",
                      title: "You have to lunch at " + name,
                      body: "At " + address + " with " + message,
                    },
                  };
                  admin.messaging().sendToDevice(fcmToken, payload)
                      .then(function(response) {
                        functions.logger.log("Successfully:", response);
                        return null;
                      })
                      .catch(function(error) {
                        functions.logger.log("Error sending message:", error);
                      });
                }
              }).catch((err) => {
                functions.logger.log("Error getting document", err);
              }));
          return null;
        }
        return null;
      }).catch((err) => {
        functions.logger.log("Error getting document", err);
      });
}
