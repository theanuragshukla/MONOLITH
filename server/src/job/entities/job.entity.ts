import {
  Entity,
  PrimaryGeneratedColumn,
  Column,
  CreateDateColumn,
  UpdateDateColumn,
} from 'typeorm';

export enum JOB_STATUS {
  RUNNING = 'RUNNING',
  COMPLETED = 'COMPLETED',
  FAILED = 'FAILED',
  PAUSED = 'PAUSED',
}

@Entity('jobs')
export class Job {
  @PrimaryGeneratedColumn()
  id: number;

  @Column()
  owner: string;

  @Column({ unique: true })
  uid: string;

  @Column()
  title: string;

  @Column()
  description: string;

  @Column()
  algo: string;

  @Column()
  scale: number;

  @Column({
    type: 'enum',
    enum: JOB_STATUS,
    default: JOB_STATUS.PAUSED,
  })
  status: JOB_STATUS;

  @CreateDateColumn({ name: 'created_at' })
  createdAt: Date;

  @UpdateDateColumn({ name: 'updated_at' })
  updatedAt: Date;
}
