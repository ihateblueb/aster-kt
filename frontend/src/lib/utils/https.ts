import ApiError from "./ApiError.ts";
import localstore from "./localstore.ts";

class Https {
    public async get(url: string, auth?: boolean) {
        this.start()

        const req = await fetch(url, {
            method: 'GET',
            headers: auth
                ? {
                    Authorization: 'Bearer ' + localstore.getParsed('token')
                }
                : {}
        });

        return await this.end(req)
    }

    public async post(url: string, auth?: boolean, body?: any) {
        this.start()

        const req = await fetch(url, {
            method: 'POST',
            headers: auth ? {
                "Content-Type": "application/json",
                Authorization: 'Bearer ' + localstore.getParsed('token')
            } : {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(body)
        });

        return await this.end(req)
    }

    public async patch(url: string, body?: any) {
        this.start()

        const req = await fetch(url, {
            method: 'PATCH',
            headers: {
                "Content-Type": "application/json",
                Authorization: 'Bearer ' + localstore.getParsed('token')
            },
            body: JSON.stringify(body)
        });

        return await this.end(req)
    }

    public async delete(url: string) {
        this.start()

        const req = await fetch(url, {
            method: 'DELETE',
            headers: {
                "Content-Type": "application/json",
                Authorization: 'Bearer ' + localstore.getParsed('token')
            }
        });

        return await this.end(req)
    }

    private start() {
    }

    private async end(res: Response) {
        let body;
        try {
            body = await res.json() ?? undefined;
        } catch { /* empty */
        }

        if (!res.ok)
            throw new ApiError(
                res.status,
                body?.message,
                body?.stackTrace
            );

        console.log(body);

        return body;
    }
}

export default new Https();
