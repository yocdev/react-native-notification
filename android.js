import {
  NativeModules,
  NativeAppEventEmitter,
} from 'react-native'

const { RNPush } = NativeModules

const RNPushAndroid = {
  ...RNPush,
  addEventListener: function (event, listener) {
    return NativeAppEventEmitter.addListener(event, listener)
  },
  removeEventListener: function (event, listener) {
    return NativeAppEventEmitter.removeListener(event, listener)
  },
  getNotifications: () => { },
  setBadgeNumber: () => { },
  getBadgeNumber: () => 0,
  clearBadge: () => { },
  requestNotification: () => { },
  checkPermission: async () => {
    return RNPush.checkPermission()
  },
  openSettingsForNotification: async () => { return RNPush.openSettingsForNotification() },
}

export default RNPushAndroid
