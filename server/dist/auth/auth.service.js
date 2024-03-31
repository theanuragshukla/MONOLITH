"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};
var __param = (this && this.__param) || function (paramIndex, decorator) {
    return function (target, key) { decorator(target, key, paramIndex); }
};
Object.defineProperty(exports, "__esModule", { value: true });
exports.AuthService = void 0;
const common_1 = require("@nestjs/common");
const typeorm_1 = require("@nestjs/typeorm");
const typeorm_2 = require("typeorm");
const User_entity_1 = require("./entities/User.entity");
const bcrypt = require("bcryptjs");
const jwt = require("jsonwebtoken");
const utils_1 = require("../utils");
let AuthService = class AuthService {
    constructor(userRepository) {
        this.userRepository = userRepository;
    }
    async signup(createUserDto) {
        const hashedPassword = await bcrypt.hash(createUserDto.password, 10);
        const newUser = this.userRepository.create({
            ...createUserDto,
            password: hashedPassword,
            uid: (0, utils_1.generateUid)(16),
        });
        await this.userRepository.save(newUser);
        const token = this.generateToken(newUser.uid);
        return { user: newUser, token };
    }
    async login(loginDto) {
        const user = await this.userRepository.findOneBy({
            email: loginDto.email,
        });
        if (!user) {
            throw new common_1.UnauthorizedException('Invalid credentials');
        }
        const passwordMatch = await bcrypt.compare(loginDto.password, user.password);
        if (!passwordMatch) {
            throw new common_1.UnauthorizedException('Invalid credentials');
        }
        const token = this.generateToken(user.uid);
        return { user, token };
    }
    async checkAuth(req, includeUser = false) {
        try {
            const token = req.cookies.token;
            const decoded = jwt.verify(token, process.env.JWT_SECRET);
            const user = await this.userRepository.findOne(decoded['data']);
            if (!user) {
                throw new common_1.UnauthorizedException('User not found');
            }
            return { status: true, user: includeUser ? user : null };
        }
        catch (error) {
            throw new common_1.UnauthorizedException('Invalid or expired token');
        }
    }
    generateToken(uid) {
        const payload = { data: uid };
        const token = jwt.sign(payload, process.env.JWT_SECRET, {
            expiresIn: '1h',
        });
        return token;
    }
};
exports.AuthService = AuthService;
exports.AuthService = AuthService = __decorate([
    (0, common_1.Injectable)(),
    __param(0, (0, typeorm_1.InjectRepository)(User_entity_1.User)),
    __metadata("design:paramtypes", [typeorm_2.Repository])
], AuthService);
//# sourceMappingURL=auth.service.js.map