import { User } from './user';

export interface Icourse {
	id: number;
	title: string;
	startDateTime: string;
	endDateTime: string;
	levelStudying: string;
	maxStudent: number;
	availablePlaces: number;
	teacher: User;
}
