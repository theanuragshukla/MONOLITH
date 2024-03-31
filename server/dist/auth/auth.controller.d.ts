/// <reference types="cookie-parser" />
import { AuthService } from './auth.service';
import { LoginDto } from './dto/login.dto';
import { CreateUserDto } from './dto/createUser.dto';
import { Request, Response } from 'express';
export declare class AuthController {
    private readonly authService;
    constructor(authService: AuthService);
    signup(createUserDto: CreateUserDto, res: Response): Promise<ApiResponse>;
    login(loginDto: LoginDto, res: Response): Promise<ApiResponse>;
    profile(req: Request): Promise<ApiResponse>;
    checkAuth(req: Request): Promise<ApiResponse>;
}
export interface ApiResponse {
    status: boolean;
    msg?: string;
    data?: any;
}
