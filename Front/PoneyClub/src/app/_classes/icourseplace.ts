import { ICourse } from './icourse';
import { IHorse } from './ihorse';
import { User } from './user';

export interface ICoursePlace {
	id: number;
	course: ICourse;
	horse: IHorse;
	rider: User;
}