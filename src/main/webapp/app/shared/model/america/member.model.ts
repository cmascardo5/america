import dayjs from 'dayjs';

export interface IMember {
  id?: number;
  firstName?: string | null;
  lastName?: string | null;
  birthDate?: string | null;
  gender?: string | null;
  address?: string | null;
  city?: string | null;
  state?: string | null;
  zip?: string | null;
  county?: string | null;
  country?: string | null;
  tobaccoUseIndicator?: boolean | null;
  substanceAbuseIndicator?: boolean | null;
  lastUpdateDatetime?: string | null;
}

export const defaultValue: Readonly<IMember> = {
  tobaccoUseIndicator: false,
  substanceAbuseIndicator: false,
};
