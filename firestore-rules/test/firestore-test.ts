import * as firebase from "@firebase/testing";
import * as admin from "firebase-admin";
import * as fs from "fs";
import * as uuid from "uuid";

export class FirestoreTest {
  static rules = fs.readFileSync("firestore.rules", "utf8");

  projectId = `firestore-emulator-example-${uuid.v4()}`;

  async before() {
    await firebase.loadFirestoreRules({
      projectId: this.projectId,
      rules: FirestoreTest.rules
    });
  }

  async after() {
    await Promise.all(firebase.apps().map(app => app.delete()));
  }

  app() {
    return firebase.initializeTestApp({
      projectId: this.projectId,
      auth: { uid: "alice", email: "alice@example.com" }
    });
  }

  adminApp() {
    return firebase.initializeAdminApp({
      projectId: this.projectId
    });
  }
}
