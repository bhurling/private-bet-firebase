import * as firebase from "@firebase/testing";
import { suite, test } from "mocha-typescript";
import { FirestoreTest } from "./firestore-test";

@suite
class FeedRules extends FirestoreTest {
  @test
  async "can read own feed data"() {
    const feed = this.app()
      .firestore()
      .collection("feeds")
      .doc("alice")
      .collection("bets");

    await firebase.assertSucceeds(feed.get());
  }

  @test
  async "can not write to own feed"() {
    const feed = this.app()
      .firestore()
      .collection("feeds")
      .doc("alice")
      .collection("bets");

    await firebase.assertFails(
      feed.doc("bet1").set({ statement: "I bet that..." })
    );
  }

  @test
  async "can not read bob's feed data"() {
    const feed = this.app()
      .firestore()
      .collection("feeds")
      .doc("bob")
      .collection("bets");

    await firebase.assertFails(feed.get());
  }

  @test
  async "can not write to bob's feed"() {
    const feed = this.app()
      .firestore()
      .collection("feeds")
      .doc("bob")
      .collection("bets");

    await firebase.assertFails(
      feed.doc("bet1").set({ statement: "I bet that..." })
    );
  }
}
