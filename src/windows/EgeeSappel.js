module.exports.add = function (success, error, args) {
    if (args.length <= 1 || typeof args[0] !== 'number' || args[0].length === 0 || typeof args[1] !== 'number' || args[1].length === 0) {
        error('Invalid arguments');
    } else {
        success((args[0]+args[1]));
    }
};
