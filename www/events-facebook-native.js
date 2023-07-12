var exec = require('cordova/exec')

exports.getApplicationId = function (s, f) {
  exec(s, f, 'EventsFacebookConnectPlugin', 'getApplicationId', [])
}

exports.setApplicationId = function (appId, s, f) {
  exec(s, f, 'EventsFacebookConnectPlugin', 'setApplicationId', [appId])
}

exports.getClientToken = function (s, f) {
  exec(s, f, 'EventsFacebookConnectPlugin', 'getClientToken', [])
}

exports.setClientToken = function (clientToken, s, f) {
  exec(s, f, 'EventsFacebookConnectPlugin', 'setClientToken', [clientToken])
}

exports.getApplicationName = function (s, f) {
  exec(s, f, 'EventsFacebookConnectPlugin', 'getApplicationName', [])
}

exports.setApplicationName = function (appName, s, f) {
  exec(s, f, 'EventsFacebookConnectPlugin', 'setApplicationName', [appName])
}

exports.getLoginStatus = function (force, s, f) {
  if (typeof force === 'function') {
    s = force;
    f = s;
    force = false;
  }
  exec(s, f, 'EventsFacebookConnectPlugin', 'getLoginStatus', [force])
}

exports.showDialog = function (options, s, f) {
  exec(s, f, 'EventsFacebookConnectPlugin', 'showDialog', [options])
}

exports.login = function (permissions, s, f) {
  exec(s, f, 'EventsFacebookConnectPlugin', 'login', permissions)
}

exports.loginWithLimitedTracking = function (permissions, nonce, s, f) {
  if (!nonce) {
    exec(s, f, 'EventsFacebookConnectPlugin', 'loginWithLimitedTracking', [permissions])
  } else {
    exec(s, f, 'EventsFacebookConnectPlugin', 'loginWithLimitedTracking', [permissions, nonce])
  }
}

exports.checkHasCorrectPermissions = function (permissions, s, f) {
  exec(s, f, 'EventsFacebookConnectPlugin', 'checkHasCorrectPermissions', permissions)
}

exports.isDataAccessExpired = function (s, f) {
  exec(s, f, 'EventsFacebookConnectPlugin', 'isDataAccessExpired', [])
}

exports.reauthorizeDataAccess = function (s, f) {
  exec(s, f, 'EventsFacebookConnectPlugin', 'reauthorizeDataAccess', [])
}

exports.setAutoLogAppEventsEnabled = function (enabled, s, f) {
  exec(s, f, 'EventsFacebookConnectPlugin', 'setAutoLogAppEventsEnabled', [enabled]);
}

exports.setAdvertiserIDCollectionEnabled = function (enabled, s, f) {
  exec(s, f, 'EventsFacebookConnectPlugin', 'setAdvertiserIDCollectionEnabled', [enabled]);
}

exports.setAdvertiserTrackingEnabled = function (enabled, s, f) {
  exec(s, f, 'EventsFacebookConnectPlugin', 'setAdvertiserTrackingEnabled', [enabled]);
}

exports.setDataProcessingOptions = function (options, country, state, s, f) {
  if (!(country >= 0 && state >= 0)) {
    exec(s, f, 'EventsFacebookConnectPlugin', 'setDataProcessingOptions', [options]);
  } else {
    exec(s, f, 'EventsFacebookConnectPlugin', 'setDataProcessingOptions', [options, country, state]);
  }
}

exports.setUserData = function (userData, s, f) {
  exec(s, f, 'EventsFacebookConnectPlugin', 'setUserData', [userData])
}

exports.clearUserData = function (s, f) {
  exec(s, f, 'EventsFacebookConnectPlugin', 'clearUserData', [])
}

exports.logEvent = function (name, params, valueToSum, s, f) {
  // Prevent NSNulls getting into iOS, messes up our [command.argument count]
  if (!params && !valueToSum) {
    exec(s, f, 'EventsFacebookConnectPlugin', 'logEvent', [name])
  } else if (params && !valueToSum) {
    exec(s, f, 'EventsFacebookConnectPlugin', 'logEvent', [name, params])
  } else if (params && valueToSum) {
    exec(s, f, 'EventsFacebookConnectPlugin', 'logEvent', [name, params, valueToSum])
  } else {
    f('Invalid arguments')
  }
}

exports.logPurchase = function (value, currency, params, s, f) {
  if (typeof params === 'function') {
    s = params;
    f = s;
    params = undefined;
  }
  if (!params) {
    exec(s, f, 'EventsFacebookConnectPlugin', 'logPurchase', [value, currency])
  } else {
    exec(s, f, 'EventsFacebookConnectPlugin', 'logPurchase', [value, currency, params])
  }
}

exports.getAccessToken = function (s, f) {
  exec(s, f, 'EventsFacebookConnectPlugin', 'getAccessToken', [])
}

exports.logout = function (s, f) {
  exec(s, f, 'EventsFacebookConnectPlugin', 'logout', [])
}

exports.getCurrentProfile = function (s, f) {
  exec(s, f, 'EventsFacebookConnectPlugin', 'getCurrentProfile', [])
}

exports.api = function (graphPath, permissions, httpMethod, s, f) {
  permissions = permissions || []
  if (typeof httpMethod === 'function') {
    s = httpMethod;
    f = s;
    httpMethod = undefined;
  }
  if (httpMethod) {
    httpMethod = httpMethod.toUpperCase();
    if (httpMethod != 'POST' && httpMethod != 'DELETE') {
      httpMethod = undefined;
    }
  }
  if (!httpMethod) {
    exec(s, f, 'EventsFacebookConnectPlugin', 'graphApi', [graphPath, permissions])
  } else {
    exec(s, f, 'EventsFacebookConnectPlugin', 'graphApi', [graphPath, permissions, httpMethod])
  }
}

exports.getDeferredApplink = function (s, f) {
  exec(s, f, 'EventsFacebookConnectPlugin', 'getDeferredApplink', [])
}

exports.activateApp = function (s, f) {
  exec(s, f, 'EventsFacebookConnectPlugin', 'activateApp', [])
}
