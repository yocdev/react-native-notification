import {
  NativeModules,
  Platform,
  NativeEventEmitter,
} from 'react-native'

const { RNCNotification } = NativeModules

const notificationEmitter = new NativeEventEmitter(RNCNotification)

const RNPush = {
  addEventListener: (event, handler) => {
      notificationEmitter.addListener(event, handler)
  },
  removeEventListener:(event,listener)=>{
    notificationEmitter.removeEventListener(event,listener)
  },
  getRegistrationId: () => {
    console.info('[RNCNotification] getRegistrationId')
    return RNCNotification.getRegistrationId()
  },
  clearBadge: () => {
    RNCNotification.setBadge(0)
  },
  init: () => {
    console.info('[RNCNotification] init')
    return RNCNotification.init()
  },
  stop: () => { },
  resume: () => { },
  removeAllNotifications: () => {
    RNCNotification.removeAllNotifications()
  },
  removeNotifications: (id) => {
  }
}

export default RNPush
