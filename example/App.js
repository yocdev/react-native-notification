import React from 'react'

import {
  View,
  Text,
  Button,
  NativeAppEventEmitter,
} from 'react-native'

import RNPush from 'react-native-notification'

export default class App extends React.Component {
  constructor() {
    super()
    this.state = {
      registrationId: 'loading',
    }
  }

  async componentDidMount() {
    RNPush.initPush()

    RNPush.getRegistrationId()
      .then((id) => {
        if (id.length) {
          this.setState({
            registrationId: id,
          })
        }
      })

    NativeAppEventEmitter.addListener('getRegistrationId', (data) => {
      this.setState({
        registrationId: data.registrationId,
      })
    })
	}

  componentWillUnmount() {
    NativeAppEventEmitter.removeAllListeners()
  }

  handleClearPress = async () => {
    await RNPush.clearAllNotifications()
  }

  render() {
    return (
      <View style={styles.container}>
        <Text>registrationId: {this.state.registrationId}</Text>
        <View>
          <Button title="clearNotification" onPress={this.handleClearPress} />
        </View>
      </View>
    )
  }
}

const styles = {
  container: {
    flex: 1,
    padding: 20,
  },
}
