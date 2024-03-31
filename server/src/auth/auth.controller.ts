import {
  Body,
  Controller,
  Post,
  Get,
  Req,
  Res,
  UseGuards,
} from '@nestjs/common';
import { AuthService } from './auth.service';
import { LoginDto } from './dto/login.dto';
import { CreateUserDto } from './dto/createUser.dto';
import { Request, Response } from 'express';
import { AuthMiddleware } from 'src/middlewares/auth.middleware';

@Controller('auth')
export class AuthController {
  constructor(private readonly authService: AuthService) {}

  @Post('signup')
  async signup(
    @Body() createUserDto: CreateUserDto,
    @Res() res: Response,
  ): Promise<ApiResponse> {
    try {
      const { user, token } = await this.authService.login(createUserDto);
      res.cookie('token', token, {
        httpOnly: true,
        secure: process.env.NODE_ENV === 'production',
        sameSite: 'strict',
        maxAge: 7 * 24 * 60 * 60 * 1000,
      });
      return {
        status: true,
        msg: 'Signup successful',
        data: user,
      };
    } catch (error) {
      return {
        status: false,
        msg: 'Signup failed',
        data: null,
      };
    }
  }

  @Post('login')
  async login(
    @Body() loginDto: LoginDto,
    @Res({ passthrough: true }) res: Response,
  ): Promise<ApiResponse> {
    try {
      const { user, token } = await this.authService.login(loginDto);
      res.cookie('token', token, {
        httpOnly: true,
        secure: process.env.NODE_ENV === 'production',
        sameSite: 'strict',
        maxAge: 7 * 24 * 60 * 60 * 1000,
      });
      return {
        status: true,
        msg: 'Login successful',
        data: user,
      };
    } catch (error) {
      return {
        status: false,
        msg: 'Login failed',
        data: null,
      };
    }
  }

  @Get('profile')
  @UseGuards(AuthMiddleware)
  async profile(@Req() req: Request): Promise<ApiResponse> {
    try {
      const { status, user } = await this.authService.checkAuth(req, true);
      if (!status) return { status: false, msg: 'Failed to retrieve profile' };
      return {
        status: true,
        msg: 'Profile retrieved successfully',
        data: user,
      };
    } catch (error) {
      return {
        status: false,
        msg: 'Failed to retrieve profile',
        data: null,
      };
    }
  }

  @Get('checkAuth')
  async checkAuth(@Req() req: Request): Promise<ApiResponse> {
    try {
      const {status} = await this.authService.checkAuth(req);
      if (!status) return { status: false, msg: 'Authentication failed' };
      return {
        status: true,
        msg: 'Authentication successful',
      };
    } catch (error) {
      return {
        status: false,
        msg: 'Authentication failed',
      };
    }
  }
}

export interface ApiResponse {
  status: boolean;
  msg?: string;
  data?: any;
}
