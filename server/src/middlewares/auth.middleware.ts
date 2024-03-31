import { Injectable, NestMiddleware } from '@nestjs/common';
import { Request, Response, NextFunction } from 'express';
import * as jwt from 'jsonwebtoken';

@Injectable()
export class AuthMiddleware implements NestMiddleware {
  use(req: Request, res: Response, next: NextFunction) {
    const token = req.cookies.token;

    if (!token) {
      const response = { status: false, msg: 'Authorization token not provided' };
      return res.status(401).json(response);
    }

    try {
      const decoded = jwt.verify(token, process.env.JWT_SECRET);
      req['user']['uid'] = decoded['data'];
      next();
    } catch (error) {
      const response = { status: false, msg: 'Invalid or expired token' };
      return res.status(401).json(response);
    }
  }
}

