import * as firebase from "@firebase/testing";
import { suite, test } from "mocha-typescript";
import { FirestoreTest } from "./firestore-test";

@suite
class LinkRules extends FirestoreTest {
  @test
  async "can write to own outgoing links"() {
    const outgoing = this.app()
      .firestore()
      .collection("links")
      .doc("alice")
      .collection("outgoing");

    await firebase.assertSucceeds(outgoing.doc("bob").set({ linked: true }));
  }

  @test
  async "can not write to own incoming links"() {
    const incoming = this.app()
      .firestore()
      .collection("links")
      .doc("alice")
      .collection("incoming");

    await firebase.assertFails(incoming.doc("bob").set({ linked: true }));
  }

  @test
  async "can not write to bob\'s outgoing links"() {
    const outgoing = this.app()
      .firestore()
      .collection("links")
      .doc("bob")
      .collection("outgoing");

    await firebase.assertFails(outgoing.doc("alice").set({ linked: true }));
  }

  @test
  async "can write to bob\'s incoming links"() {
    const incoming = this.app()
      .firestore()
      .collection("links")
      .doc("bob")
      .collection("incoming");

    await firebase.assertSucceeds(incoming.doc("alice").set({ linked: true }));
  }

  @test
  async "can not write to bob\s incoming links for another user"() {
    const incoming = this.app()
      .firestore()
      .collection("links")
      .doc("bob")
      .collection("incoming");

    await firebase.assertFails(incoming.doc("charlie").set({ linked: true }));
  }
}
