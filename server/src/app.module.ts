import { MiddlewareConsumer, Module, RequestMethod } from '@nestjs/common';
import { AppController } from './app.controller';
import { AppService } from './app.service';
import { JobModule } from './job/job.module';
import { TypeOrmModule } from '@nestjs/typeorm';
import { AuthMiddleware } from './middlewares/auth.middleware';
import { AuthModule } from './auth/auth.module';
import { ConfigModule } from '@nestjs/config';
import { JobController } from './job/job.controller';

@Module({
  imports: [
    TypeOrmModule.forRoot({
      type: 'postgres',
      host: 'localhost',
      port: 5432,
      username: 'anurag',
      password: 'asdf1234',
      database: 'monolith',
      logger: 'simple-console',
      entities: [__dirname + '/*/entities/*.entity.{js,ts}'],
      synchronize: true,
    }),
    ConfigModule.forRoot({
      isGlobal: true,
    }),

    JobModule,
    AuthModule,
  ],
  controllers: [AppController],
  providers: [AppService],
})
export class AppModule {
  configure(consumer: MiddlewareConsumer) {
    consumer
      .apply(AuthMiddleware)
      .forRoutes(JobController, {
        path: 'auth/profile',
        method: RequestMethod.GET,
      });
  }
}
