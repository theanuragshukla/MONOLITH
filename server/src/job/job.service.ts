import { Injectable } from '@nestjs/common';
import { CreateJobDto } from './dto/create-job.dto';
import { Repository } from 'typeorm';
import { JOB_STATUS, Job } from './entities/job.entity';
import { InjectRepository } from '@nestjs/typeorm';
import { generateUid } from 'src/utils';

@Injectable()
export class JobService {
  constructor(@InjectRepository(Job) private jobs: Repository<Job>) {}
  async create(createJobDto: CreateJobDto, uid: string) {
    try {
      const job = new Job();
      job.owner = uid;
      job.uid = generateUid();
      job.title = createJobDto.title;
      job.description = createJobDto.description;
      job.algo = createJobDto.algo;
      job.scale = createJobDto.scale;
      await this.jobs.save(job);
      return job;
    } catch (error) {
      throw new Error(error);
    }
  }

  findAll(uid: string) {
    try {
      return this.jobs.findBy({ owner: uid });
    } catch (error) {
      throw new Error(error);
    }
  }

  findOne(uid: string, jobId: string) {
    try {
      return this.jobs.findOneBy({ owner: uid, uid: jobId });
    } catch (error) {
      throw new Error(error);
    }
  }

  async pauseExecution(uid: string, jobId: string) {
    try {
      const job = await this.jobs.findOneBy({ owner: uid, uid: jobId });
      job.status = JOB_STATUS.PAUSED;
      return this.jobs.save(job);
    } catch (error) {
      throw new Error(error);
    }
  }

  async resumeExecution(uid: string, jobId: string) {
    try {
      const job = await this.jobs.findOneBy({ owner: uid, uid: jobId });
      job.status = JOB_STATUS.RUNNING;
      return this.jobs.save(job);
    } catch (error) {
      throw new Error(error);
    }
  }

  getExecutions(id: number) {
    return `This action returns all executions of a #${id} job`;
  }

  async remove(uid: string, jobId: string) {
    try {
      const job = await this.jobs.findOneBy({ owner: uid, uid: jobId });
      return this.jobs.remove(job);
    } catch (error) {
      throw new Error(error);
    }
  }
}
