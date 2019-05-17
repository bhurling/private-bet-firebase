import * as firebase from "@firebase/testing";
import { suite, test } from "mocha-typescript";
import { FirestoreTest } from "./firestore-test";

@suite
class ProfileRules extends FirestoreTest {
  @test
  async "can read own public profile data"() {
    const profile = this.app()
      .firestore()
      .collection("public_profiles")
      .doc("alice");

    await firebase.assertSucceeds(profile.get());
  }

  @test
  async "can read bob's public profile data"() {
    const profile = this.app()
      .firestore()
      .collection("public_profiles")
      .doc("bob");

    await firebase.assertSucceeds(profile.get());
  }

  @test
  async "can read own private profile data"() {
    const profile = this.app()
      .firestore()
      .collection("private_profiles")
      .doc("alice");

    await firebase.assertSucceeds(profile.get());
  }

  @test
  async "can not read bob's private profile data"() {
    const profile = this.app()
      .firestore()
      .collection("private_profiles")
      .doc("bob");

    await firebase.assertFails(profile.get());
  }
}
