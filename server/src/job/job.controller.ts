import {
  Controller,
  Get,
  Post,
  Body,
  Patch,
  Param,
  Delete,
  Req,
} from '@nestjs/common';
import { JobService } from './job.service';
import { CreateJobDto } from './dto/create-job.dto';
import { Request } from 'express';

@Controller('job')
export class JobController {
  constructor(private readonly jobService: JobService) {}
  @Post()
  create(@Body() createJobDto: CreateJobDto, @Req() req: Request) {
    const uid = req['user']['uid'];
    return this.jobService.create(createJobDto, uid);
  }

  @Get('all')
  findAll(@Req() req: Request) {
    const uid = req['user']['uid'];
    return this.jobService.findAll(uid);
  }

  @Get(':id')
  findOne(@Param('id') id: string, @Req() req: Request) {
    const uid = req['user']['uid'];
    return this.jobService.findOne(uid, id);
  }

  @Get('tasks/:id')
  getExecutions(@Param('id') id: string, @Req() req: Request ) {
    const uid = req['user']['uid'];
    return this.jobService.getExecutions(+id);
  }

  @Patch('pause/:id')
  pauseExecution(@Param('id') id: string, @Req() req: Request) {
    const uid = req['user']['uid'];
    return this.jobService.pauseExecution(uid, id);
  }

  @Patch('resume/:id')
  resumeExecution(@Param('id') id: string, @Req() req: Request) {
    const uid = req['user']['uid'];
    return this.jobService.resumeExecution(uid, id);
  }

  @Delete(':id')
  remove(@Param('id') id: string, @Req() req: Request) {
    const uid = req['user']['uid'];
    return this.jobService.remove(uid, id);
  }
}
