/// <reference types="cookie-parser" />
import { Repository } from 'typeorm';
import { User } from './entities/User.entity';
import { LoginDto } from './dto/login.dto';
import { CreateUserDto } from './dto/createUser.dto';
import { Request } from 'express';
export declare class AuthService {
    private userRepository;
    constructor(userRepository: Repository<User>);
    signup(createUserDto: CreateUserDto): Promise<{
        user: User;
        token: string;
    }>;
    login(loginDto: LoginDto): Promise<{
        user: User;
        token: string;
    }>;
    checkAuth(req: Request, includeUser?: boolean): Promise<{
        status: boolean;
        user: User;
    }>;
    private generateToken;
}
