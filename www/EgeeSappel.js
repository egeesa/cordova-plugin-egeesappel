var exec = require('cordova/exec');

var PLUGIN_NAME = 'EgeeSappel';

function EgeeSappel() {}

EgeeSappel.prototype.getLicence = function (success, error) {
    exec(success, error, PLUGIN_NAME, 'getLicence', []);
};

EgeeSappel.prototype.radioInterpret = function (args0,success, error) {
    exec(success, error, PLUGIN_NAME, 'radioInterpret', [args0]);
};

EgeeSappel.prototype.radioInterpretHead = function (args0,success, error) {
    exec(success, error, PLUGIN_NAME, 'radioInterpretHead', [args0]);
};

module.exports = new EgeeSappel();
