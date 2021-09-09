import {
  NativeModules,
  NativeEventEmitter,
  EmitterSubscription,
} from 'react-native'
import { RNCNotification, RNCRegisterRes } from './type'



export enum RNPushEvent {
  Register = 'register',
  RegistrationError = 'registrationError',
  Notification = 'notification',
  OpenNotification = 'openNotification',
  NotificationAuthorization = 'NotificationAuthorization',
}

export enum RNNotificationAuthorization{
  NotDetermined = 0,
  Denied = 1,
  Authorized = 2,
  Provisional = 3,
}

const { RNPush } = NativeModules

const notificationEmitter = new NativeEventEmitter(RNPush)

export type ReactNativeNRegisterListener = (data: RNCRegisterRes) => void
export type ReactNativeNotificationListener = (data: RNCNotification) => void
export type ReactnativeNotificationAuthorizationListener = (data: RNNotificationAuthorization) => void

class ReactNativeNotification {

  subscriptions:EmitterSubscription[]=[]

  public async init() {
    return RNPush.init()
  }

  public async resume() {
    return RNPush.resume()
  }

  public async stop() {
    return RNPush.stop()
  }

  public async getRegistrationId(): Promise<string> {
    return RNPush.getRegistrationId()
  }

  public async requestNotification(): Promise<boolean> {
    return RNPush.requestNotification()
  }

  public async setBadge(badge: number) {
    return RNPush.setBadge(badge)
  }

  public addEventListener(event: RNPushEvent, handler) {

  }

  public removeEventListener(event: RNPushEvent, handler) {

  }

  public addRegisterListener(listener: ReactNativeNRegisterListener): EmitterSubscription {
    const subscription = notificationEmitter.addListener(RNPushEvent.Register, listener)
    this.subscriptions.push(subscription)
    return subscription
  }

  public addNotificationListener(listener: ReactNativeNotificationListener): EmitterSubscription {
    const realListener = (data) => {
      try {
        const extras = this._getExtras(data)
        listener({
          id:data.id,
          title:data.title,
          content:data.content,
          extras,
        })
      } catch (e) {
        console.error('[ReactNativeNotification] ReactNativeNotificationListener', e)
      }
    }
    const subscription = notificationEmitter.addListener(RNPushEvent.Notification, realListener)
    this.subscriptions.push(subscription)
    return subscription
  }

  public addOpenNotificationListener(listener:ReactNativeNotificationListener):EmitterSubscription{
    const realListener = (data) => {
      try {
        const extras = this._getExtras(data)
        listener({
          id:data.id,
          title:data.title,
          content:data.content,
          extras,
        })
      } catch (e) {
        console.error('[ReactNativeNotification] ReactNativeNotificationListener', e)
      }
    }
    const subscription = notificationEmitter.addListener(RNPushEvent.OpenNotification, realListener)
    this.subscriptions.push(subscription)
    return subscription
  }

  public addNotificationAuthorizationListener(listener:ReactnativeNotificationAuthorizationListener):EmitterSubscription{
    const subscription = notificationEmitter.addListener(RNPushEvent.NotificationAuthorization,listener)
    this.subscriptions.push(subscription)
    return subscription
  }

  public removeAllListeners(){
    this.subscriptions.map(it=>it?.remove())
  }


  _getExtras(data) {
    try {
      const extras = JSON.parse(data.extras)
      return extras
    } catch (e) {
      console.error('[ReactNativeNotification] _getExtras', e)
    }
  }
}

const push = new ReactNativeNotification()

export default push