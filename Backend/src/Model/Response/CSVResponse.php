<?php


namespace App\Model\Response;


use Cake\Http\Response;

class CSVResponse extends Response
{
    /**
     * FileResponse constructor.
     *
     * @param string $fileName
     * @param string $filePath
     * @param string $mimeType
     *
     * @throws \InvalidArgumentException
     */
    public function __construct(string $fileName, string $body)
    {
        $this->fileName = $fileName;
        $this->mimeType = 'text/csv';

        parent::__construct([
            'status'  => 200,
            'type'    => 'text/csv',
            'charset' => 'utf-8',
            'body'    => $body,
        ]);
    }

    /**
     * Create an instance of response.
     *
     * @param string $fileName File name.
     * @param array  $headers  CSV file headers (first row of file)
     * @param array  $data     Data to be exported.
     *
     * @return \App\Model\Response\CSVResponse
     */
    public static function make(string $fileName, array $data, array $headers = [])
    {
        $buffer = fopen('php://temp', 'r+');

        if (!empty($headers)) {
            fputcsv($buffer, $headers);
        }

        foreach ($data as $line) {
            fputcsv($buffer, $line);
        }

        rewind($buffer);
        $csv = stream_get_contents($buffer);
        fclose($buffer);

        $response = new static($fileName, $csv);

        return $response
            ->withHeader('Content-disposition', 'attachment; filename="' . $fileName . '"');
    }
}