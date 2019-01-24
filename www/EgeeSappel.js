var exec = require('cordova/exec');

var PLUGIN_NAME = 'EgeeSappel';
function EgeeSappel() {}

EgeeSappel.prototype.getLicence = function (success, error) {
    exec(success, error, PLUGIN_NAME, "getLicence", []);
};

module.exports = new EgeeSappel();
