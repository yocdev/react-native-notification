import {
  NativeModules,
  NativeEventEmitter,
} from 'react-native'

const { RNCNotification } = NativeModules

const notificationEmitter = new NativeEventEmitter(RNCNotification)

const RNPush = {
  addEventListener: (event, handler) => {
    notificationEmitter.addListener(event, handler)
  },
  removeEventListener: (event, listener) => {
    notificationEmitter.removeEventListener(event, listener)
  },
  getRegistrationId: () => {
    return RNCNotification.getRegistrationId()
  },
  clearBadge: () => {
    RNCNotification.setBadge(0)
  },
  init: () => {
    return RNCNotification.init()
  },
  stop: () => { },
  resume: () => { },
  removeAllNotifications: () => {
    RNCNotification.removeAllNotifications()
  },
  removeNotifications: (id) => {
  },
  requestNotification: async () => {
    return RNCNotification.requestNotification()
  },
  checkPromission: async () => {
    const state = await RNCNotification.requestNotification()
    return state > 1
  },
  openSettingsForNotification: async () => {
    return RNCNotification.openSettingsForNotification()
  }
}

export default RNPush
