import { CreateJobDto } from './dto/create-job.dto';
import { Repository } from 'typeorm';
import { Job } from './entities/job.entity';
export declare class JobService {
    private jobs;
    constructor(jobs: Repository<Job>);
    create(createJobDto: CreateJobDto, uid: string): Promise<Job>;
    findAll(uid: string): Promise<Job[]>;
    findOne(uid: string, jobId: string): Promise<Job>;
    pauseExecution(uid: string, jobId: string): Promise<Job>;
    resumeExecution(uid: string, jobId: string): Promise<Job>;
    getExecutions(id: number): string;
    remove(uid: string, jobId: string): Promise<Job>;
}
