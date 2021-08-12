import {
  NativeModules,
  Platform,
  NativeEventEmitter,
} from 'react-native'

// import PushNotificationIOS from '@react-native-community/push-notification-ios'

const { RNCNotification } = NativeModules

// let registrationIdIOS = ''
// PushNotificationIOS.addEventListener('register', (token) => {
// 	registrationIdIOS = token
// })
const notificationEmitter = new NativeEventEmitter(RNCNotification)

const RNPush = {
  addEventListener: (event, handler) => {
    if (event === 'notification') {
      notificationEmitter.addListener(event, handler)
    }
    // if (event === 'register') {
    //   return PushNotificationIOS.addEventListener('register', (token) => {
    //     registrationIdIOS = token
    //     handler({
    //       type: 'Apple',
    //       registrationId: token,
    //     })
    //   })
    // } else {
    //   PushNotificationIOS.addEventListener(event, handler)
    // }
  },
  getRegistrationId: () => {
    // return Promise.resolve({
    //   type: 'Apple',
    //   registrationId: registrationIdIOS,
    // })
    return RNCNotification.getRegistrationId()
  },
  clearBadge: () => {
    // PushNotificationIOS.setApplicationIconBadgeNumber(0)
  },
  init: () => {
    // have to call this to get fresh registrationId
    // PushNotificationIOS.requestPermissions()
  },
  stop: () => { },
  resume: () => { },
  removeAllNotifications: () => {
    // PushNotificationIOS.removeAllDeliveredNotifications()
  },
  removeNotifications: (id) => {
    // PushNotificationIOS.removeDeliveredNotifications(id)
  }
}

export default RNPush
