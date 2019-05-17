import * as firebase from "@firebase/testing";
import { suite, test } from "mocha-typescript";
import { FirestoreTest } from "./firestore-test";

@suite
class DeviceRules extends FirestoreTest {
  @test
  async "can read own device data"() {
    const devices = this.app()
      .firestore()
      .collection("devices")
      .doc("alice");

    await firebase.assertSucceeds(devices.get());
  }

  @test
  async "can write own device data"() {
    const devices = this.app()
      .firestore()
      .collection("devices")
      .doc("alice");

    await firebase.assertSucceeds(devices.set({ cryptictoken: true }));
  }

  @test
  async "can not read bob's device data"() {
    const devices = this.app()
      .firestore()
      .collection("devices")
      .doc("bob");

    await firebase.assertFails(devices.get());
  }

  @test
  async "can not write bob's device data"() {
    const devices = this.app()
      .firestore()
      .collection("devices")
      .doc("bob");

    await firebase.assertFails(devices.set({ cryptictoken: true }));
  }
}
