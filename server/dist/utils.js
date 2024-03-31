"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.generateUid = void 0;
const generateUid = (len = 8) => {
    const charset = 'abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789';
    let uid = '';
    for (let i = 0; i < len; i++) {
        uid += charset.charAt(Math.floor(Math.random() * charset.length));
    }
    return uid;
};
exports.generateUid = generateUid;
//# sourceMappingURL=utils.js.map