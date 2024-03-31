"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
Object.defineProperty(exports, "__esModule", { value: true });
exports.AuthMiddleware = void 0;
const common_1 = require("@nestjs/common");
const jwt = require("jsonwebtoken");
let AuthMiddleware = class AuthMiddleware {
    use(req, res, next) {
        const token = req.cookies.token;
        if (!token) {
            const response = { status: false, msg: 'Authorization token not provided' };
            return res.status(401).json(response);
        }
        try {
            const decoded = jwt.verify(token, process.env.JWT_SECRET);
            req['user']['uid'] = decoded['data'];
            next();
        }
        catch (error) {
            const response = { status: false, msg: 'Invalid or expired token' };
            return res.status(401).json(response);
        }
    }
};
exports.AuthMiddleware = AuthMiddleware;
exports.AuthMiddleware = AuthMiddleware = __decorate([
    (0, common_1.Injectable)()
], AuthMiddleware);
//# sourceMappingURL=auth.middleware.js.map