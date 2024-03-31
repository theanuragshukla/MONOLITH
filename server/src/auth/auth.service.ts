import { Injectable, UnauthorizedException } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository } from 'typeorm';
import { User } from './entities/User.entity';
import { LoginDto } from './dto/login.dto';
import { CreateUserDto } from './dto/createUser.dto';
import * as bcrypt from 'bcryptjs';
import * as jwt from 'jsonwebtoken';
import { generateUid } from 'src/utils';
import { Request } from 'express';

@Injectable()
export class AuthService {
  constructor(
    @InjectRepository(User) private userRepository: Repository<User>,
  ) {}

  async signup(createUserDto: CreateUserDto) {
    const hashedPassword = await bcrypt.hash(createUserDto.password, 10);
    const newUser = this.userRepository.create({
      ...createUserDto,
      password: hashedPassword,
      uid: generateUid(16),
    });
    await this.userRepository.save(newUser);
    const token = this.generateToken(newUser.uid);
    return { user: newUser, token };
  }

  async login(loginDto: LoginDto) {
    const user = await this.userRepository.findOneBy({
      email: loginDto.email,
    });
    if (!user) {
      throw new UnauthorizedException('Invalid credentials');
    }

    const passwordMatch = await bcrypt.compare(
      loginDto.password,
      user.password,
    );

    if (!passwordMatch) {
      throw new UnauthorizedException('Invalid credentials');
    }

    const token = this.generateToken(user.uid);

    return { user, token };
  }

  async checkAuth(req: Request, includeUser = false) {
    try {
      const token = req.cookies.token;
      const decoded = jwt.verify(token, process.env.JWT_SECRET);
      const user = await this.userRepository.findOne(decoded['data']);
      if (!user) {
        throw new UnauthorizedException('User not found');
      }
      return { status: true, user: includeUser ? user : null }
    } catch (error) {
      throw new UnauthorizedException('Invalid or expired token');
    }
  }

  private generateToken(uid: string): string {
    const payload = { data: uid };
    const token = jwt.sign(payload, process.env.JWT_SECRET, {
      expiresIn: '1h',
    });
    return token;
  }
}
