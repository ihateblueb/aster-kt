let defaults = {
	debug: {
		type: 'boolean',
		value: false
	},

	self: {
		type: 'json',
		value: undefined
	},
	token: {
		type: 'string',
		value: undefined
	},
};

class localstore {
	public defaults = defaults;

	public get(key: string) {
		if (key !== 'debug' && this.getParsed('debug'))
			console.debug('[localstore get] ' + key);

		let toReturn;

		toReturn = localStorage.getItem('aster_' + key);

		if (toReturn) {
			return toReturn;
		} else {
			return defaults[key].value;
		}
	}

	public getParsed(key: string) {
		if (key !== 'debug' && this.getParsed('debug'))
			console.debug('[localstore getParsed] ' + key);

		try {
			let defaultObj: { type: string; value: any } | undefined =
				defaults[key];
			if (!defaultObj) return undefined;

			let toReturn;
			toReturn = localStorage.getItem('aster_' + key);

			if (toReturn) {
				if (defaultObj.type === 'string') return String(toReturn);
				if (defaultObj.type === 'boolean') return Boolean(toReturn);
				if (defaultObj.type === 'number') return Number(toReturn);
				if (defaultObj.type === 'json') return JSON.parse(toReturn);
			} else {
				return defaultObj.value;
			}
		} catch (e) {
			console.error('failed getParsed of ' + key, e);
			return undefined;
		}
	}

	public set(key: string, val: string) {
		if (this.getParsed('debug'))
			console.debug('[localstore set] ' + key, val);

		if (val) {
			// a 'false' string is considered true!
			localStorage.setItem('aster_' + key, val);
		} else {
			localStorage.setItem('aster_' + key, '');
		}

		return;
	}

	public delete(key: string) {
		if (this.getParsed('debug'))
			console.debug('[localstore delete] ' + key);

		localStorage.removeItem('aster_' + key);
	}
}

export default new localstore();
