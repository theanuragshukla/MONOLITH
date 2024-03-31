"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};
var __param = (this && this.__param) || function (paramIndex, decorator) {
    return function (target, key) { decorator(target, key, paramIndex); }
};
Object.defineProperty(exports, "__esModule", { value: true });
exports.JobService = void 0;
const common_1 = require("@nestjs/common");
const typeorm_1 = require("typeorm");
const job_entity_1 = require("./entities/job.entity");
const typeorm_2 = require("@nestjs/typeorm");
const utils_1 = require("../utils");
let JobService = class JobService {
    constructor(jobs) {
        this.jobs = jobs;
    }
    async create(createJobDto, uid) {
        try {
            const job = new job_entity_1.Job();
            job.owner = uid;
            job.uid = (0, utils_1.generateUid)();
            job.title = createJobDto.title;
            job.description = createJobDto.description;
            job.algo = createJobDto.algo;
            job.scale = createJobDto.scale;
            await this.jobs.save(job);
            return job;
        }
        catch (error) {
            throw new Error(error);
        }
    }
    findAll(uid) {
        try {
            return this.jobs.findBy({ owner: uid });
        }
        catch (error) {
            throw new Error(error);
        }
    }
    findOne(uid, jobId) {
        try {
            return this.jobs.findOneBy({ owner: uid, uid: jobId });
        }
        catch (error) {
            throw new Error(error);
        }
    }
    async pauseExecution(uid, jobId) {
        try {
            const job = await this.jobs.findOneBy({ owner: uid, uid: jobId });
            job.status = job_entity_1.JOB_STATUS.PAUSED;
            return this.jobs.save(job);
        }
        catch (error) {
            throw new Error(error);
        }
    }
    async resumeExecution(uid, jobId) {
        try {
            const job = await this.jobs.findOneBy({ owner: uid, uid: jobId });
            job.status = job_entity_1.JOB_STATUS.RUNNING;
            return this.jobs.save(job);
        }
        catch (error) {
            throw new Error(error);
        }
    }
    getExecutions(id) {
        return `This action returns all executions of a #${id} job`;
    }
    async remove(uid, jobId) {
        try {
            const job = await this.jobs.findOneBy({ owner: uid, uid: jobId });
            return this.jobs.remove(job);
        }
        catch (error) {
            throw new Error(error);
        }
    }
};
exports.JobService = JobService;
exports.JobService = JobService = __decorate([
    (0, common_1.Injectable)(),
    __param(0, (0, typeorm_2.InjectRepository)(job_entity_1.Job)),
    __metadata("design:paramtypes", [typeorm_1.Repository])
], JobService);
//# sourceMappingURL=job.service.js.map