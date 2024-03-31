export declare enum JOB_STATUS {
    RUNNING = "RUNNING",
    COMPLETED = "COMPLETED",
    FAILED = "FAILED",
    PAUSED = "PAUSED"
}
export declare class Job {
    id: number;
    owner: string;
    uid: string;
    title: string;
    description: string;
    algo: string;
    scale: number;
    status: JOB_STATUS;
    createdAt: Date;
    updatedAt: Date;
}
