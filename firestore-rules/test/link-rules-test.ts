import * as firebase from "@firebase/testing";
import { suite, test } from "mocha-typescript";
import { FirestoreTest } from "./firestore-test";

@suite
class LinkRules extends FirestoreTest {
  @test
  async "can read own confirmed links"() {
    const confirmed = this.app()
          .firestore()
          .collection("links")
          .doc("alice")
          .collection("confirmed")

    await firebase.assertSucceeds(confirmed.get());
  }

  @test
  async "can write to own outgoing links"() {
    const outgoing = this.app()
      .firestore()
      .collection("links")
      .doc("alice")
      .collection("outgoing")
      .doc("bob");

    await firebase.assertSucceeds(outgoing.set({ linked: true }));
  }

  @test
  async "can not write outgoing link to herself"() {
    const outgoing = this.app()
      .firestore()
      .collection("links")
      .doc("alice")
      .collection("outgoing")
      .doc("alice");

    await firebase.assertFails(outgoing.set({ linked: true }));
  }

  @test
  async "can not write to own incoming links"() {
    const incoming = this.app()
      .firestore()
      .collection("links")
      .doc("alice")
      .collection("incoming")
      .doc("bob");

    await firebase.assertFails(incoming.set({ linked: true }));
  }

  @test
  async "can not write to bob's outgoing links"() {
    const outgoing = this.app()
      .firestore()
      .collection("links")
      .doc("bob")
      .collection("outgoing")
      .doc("alice");

    await firebase.assertFails(outgoing.set({ linked: true }));
  }

  @test
  async "can not write to bob's incoming links"() {
    const incoming = this.app()
      .firestore()
      .collection("links")
      .doc("bob")
      .collection("incoming")
      .doc("alice");

    await firebase.assertFails(incoming.set({ linked: true }));
  }

  @test
  async "can not write to bobs incoming links for another user"() {
    const incoming = this.app()
      .firestore()
      .collection("links")
      .doc("bob")
      .collection("incoming")
      .doc("charlie");

    await firebase.assertFails(incoming.set({ linked: true }));
  }

  @test
  async "can confirm incoming link in own collection"() {
    await this.prepareIncoming({ from: "bob", to: "alice" })

    const confirmed = this.app()
      .firestore()
      .collection("links")
      .doc("alice")
      .collection("confirmed")
      .doc("bob");

    await firebase.assertSucceeds(confirmed.set({ linked: true }));
  }

  @test
  async "can not confirm incoming link in bob's collection"() {
    await this.prepareIncoming({ from: "bob", to: "alice" })

    const confirmed = this.app()
      .firestore()
      .collection("links")
      .doc("bob")
      .collection("confirmed")
      .doc("alice");

    await firebase.assertFails(confirmed.set({ linked: true }));
  }

  @test
  async "can not confirm link if not incoming"() {
    const confirmed = this.app()
      .firestore()
      .collection("links")
      .doc("alice")
      .collection("confirmed")
      .doc("bob");

    await firebase.assertFails(confirmed.set({ linked: true }));
  }

  @test
  async "can not confirm own invitation"() {
    await this.prepareIncoming({ from: "alice", to: "bob" })

    const confirmed = this.app()
      .firestore()
      .collection("links")
      .doc("alice")
      .collection("confirmed")
      .doc("bob");

    await firebase.assertFails(confirmed.set({ linked: true }));
  }

  @test
  async "can not confirm own invitation in bob's collection"() {
    await this.prepareIncoming({ from: "alice", to: "bob" })

    const confirmed = this.app()
      .firestore()
      .collection("links")
      .doc("bob")
      .collection("confirmed")
      .doc("alice");

    await firebase.assertFails(confirmed.set({ linked: true }));
  }

  prepareIncoming(link: IncomingLink) {
    this.adminApp()
      .firestore()
      .collection("links")
      .doc(link.to)
      .collection("incoming")
      .doc(link.from)
      .set({ displayName: "My name" });
  }
}

interface IncomingLink {
  from: string;
  to: string;
}
