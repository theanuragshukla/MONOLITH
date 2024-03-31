import { Injectable } from '@nestjs/common';

@Injectable()
export class AppService {
  getHello(): { statue: boolean; msg: string } {
    return {
      statue: true,
      msg: 'Alive!',
    };
  }
}
