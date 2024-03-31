/// <reference types="cookie-parser" />
import { JobService } from './job.service';
import { CreateJobDto } from './dto/create-job.dto';
import { Request } from 'express';
export declare class JobController {
    private readonly jobService;
    constructor(jobService: JobService);
    create(createJobDto: CreateJobDto, req: Request): Promise<import("./entities/job.entity").Job>;
    findAll(req: Request): Promise<import("./entities/job.entity").Job[]>;
    findOne(id: string, req: Request): Promise<import("./entities/job.entity").Job>;
    getExecutions(id: string, req: Request): string;
    pauseExecution(id: string, req: Request): Promise<import("./entities/job.entity").Job>;
    resumeExecution(id: string, req: Request): Promise<import("./entities/job.entity").Job>;
    remove(id: string, req: Request): Promise<import("./entities/job.entity").Job>;
}
