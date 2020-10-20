import { Icourse } from './icourse';
import { IHorse } from './ihorse';
import { User } from './user';

export interface ICoursePlace {
	id: number;
	course: Icourse;
	horse: IHorse;
	rider: User;
}
