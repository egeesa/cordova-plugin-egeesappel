cordova.define("cordova.plugin.egeesappel.EgeeSappel", function(require, exports, module) {
    var exec = require('cordova/exec');
    
    module.exports.getVersion = function (success, error) {
        exec(success, error, 'EgeeSappel', 'getVersion', []);
    };

    module.exports.readDeviceConfiguration = function (success, error) {
        exec(success, error, 'EgeeSappel', 'readDeviceConfiguration', []);
    };

    module.exports.openConfigurationConnection = function (arg0,success, error) {
        exec(success, error, 'EgeeSappel', 'openConfigurationConnection', [arg0]);
    };

    module.exports.initializeConfiguration = function (arg0,success, error) {
        exec(success, error, 'EgeeSappel', 'initializeConfiguration', [arg0]);
    };

    module.exports.closeConfigConnection = function (success, error) {
        exec(success, error, 'EgeeSappel', 'closeConfigConnection', []);
    };

    module.exports.setDeviceConfiguration = function (arg0,success, error) {
        exec(success, error, 'EgeeSappel', 'setDeviceConfiguration', [arg0]);
    };

    module.exports.openConnection = function (arg0, success, error) {
        exec(success, error, 'EgeeSappel', 'openConnection', [arg0]);
    };

    module.exports.pollFrames = function (success, error) {
        exec(success, error, 'EgeeSappel', 'pollFrames', []);
    };

    module.exports.closeConnection = function (success, error) {
        exec(success, error, 'EgeeSappel', 'closeConnection', []);
    };

    module.exports.openConnectionStatic = function (arg0, success, error) {
        exec(success, error, 'EgeeSappel', 'openConnectionStatic', [arg0]);
    };

    module.exports.pollFrameStatic = function (success, error) {
        exec(success, error, 'EgeeSappel', 'pollFrameStatic', []);
    };

    module.exports.closeConnectionStatic = function (success, error) {
        exec(success, error, 'EgeeSappel', 'closeConnectionStatic', []);
    };
});
