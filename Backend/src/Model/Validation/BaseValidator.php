<?php

namespace App\Model\Validation;

use App\Model\Enum\Environment;
use App\Service\JwtHelperService;
use Cake\Core\Configure;
use Cake\ORM\TableRegistry;
use Cake\Validation\Validation;
use Cake\Validation\Validator;
use ReCaptcha\ReCaptcha;

/**
 * Class BaseValidator
 *
 * @package App\Model\Validation
 */
class BaseValidator extends Validator
{
    public function __construct()
    {
        parent::__construct();

        $this->setProvider('base', self::class);
    }

    // It is used to check single file extenstion.
    public function allowExtension($field, array $extensionList, string $message)
    {
        return $this->add($field, 'extension', [
            'rule'     => ['checkFileExtension', $extensionList],
            'message'  => $message,
            'provider' => 'base',
        ]);
    }

    // It is used to check single file extenstion with lower extentions.
    public static function checkFileExtension($file, array $extensionList = []): bool
    {
        if (empty($file['name'])) {
            return false;
        }

        $fileName  = $file['name'];
        $extension = strtolower(pathinfo($fileName, PATHINFO_EXTENSION));
        foreach ($extensionList as $key => $value) {
            $extensionList[$key] = strtolower($value);
        }

        return in_array($extension, $extensionList);
    }

    // It is used to check multiple file extenstion.
    public function allowMultipleFileExtension($field, array $extensionList, string $message)
    {
        return $this->add($field, 'extension', [
            'rule'     => ['checkMultipleFileExtension', $extensionList],
            'message'  => $message,
            'provider' => 'base',
        ]);
    }

    // It is used to check multiple file extenstion with lower extentions.
    public static function checkMultipleFileExtension($files, array $extensionList = []): bool
    {
        $extension = [];
        foreach ($files as $file) {
            if (empty($file['name'])) {
                return false;
            }

            $fileName    = $file['name'];
            $extension[] = strtolower(pathinfo($fileName, PATHINFO_EXTENSION));
        }

        foreach ($extensionList as $key => $value) {
            $extensionList[$key] = strtolower($value);
        }

        return !array_diff($extension, $extensionList);
    }

    public function dateTimeISO8601($field, $formats = ['ymd'], $message = null, $when = null)
    {
        $extra = array_filter([
            'on'      => $when,
            'message' => $message ?? 'Provided value is not a valid ISO 8601 datetime value.',
        ]);

        return $this->add($field, 'dateTime', $extra + [
                'rule'     => ['iso8601dateTime', $formats],
                'provider' => 'base',
            ]);
    }

    public function timezone($field, $message = null, $when = null)
    {
        $extra = array_filter([
            'on'      => $when,
            'message' => $message ?? 'Provided value is not a valid IANA timezone identifier.',
        ]);

        return $this->add($field, 'validValue', $extra + [
                'rule'     => ['ianaTimezone'],
                'provider' => 'base',
            ]);
    }

    public function fullName($field, $when = null)
    {
        return $this
            ->lengthBetween($field, [1, 255], 'Name is invalid')
            ->add($field, 'validFormat', [
                'on'      => $when,
                'rule'    => ['custom', '/^(?=.*[a-zA-Z])(?:[\p{L}\p{Mn}\p{Pd}\'\x{2019}\.]+(?:$|\s+)){1,}$/u'],
                'message' => 'Name is invalid',
            ]);
    }

    public function addressLine1($field, $when = null)
    {
        return $this
            ->lengthBetween(
                $field,
                [3, 255],
                'Address line 1 contains invalid characters',
                $when
            )
            ->add($field, [
                'validFormat' => [
                    'on'      => $when,
                    'rule'    => ['custom', '/((?:[A-Za-z0-9\-\—\ \'\/\,\.\(\)\!\@\$\%\#]+$)*[a-zA-Z0-9])/'],
                    'message' => 'Address line 1 contains invalid characters',
                ],
            ]);
    }

    public function addressLine2($field, $when = null)
    {
        return $this
            ->lengthBetween(
                $field,
                [3, 255],
                'Address line 2 contains invalid characters',
                $when
            )->add($field, [
                'validFormat' => [
                    'on'      => $when,
                    'rule'    => ['custom', '/((?:[A-Za-z0-9\-\—\ \'\/\,\.\(\)\!\@\$\%\#]+$)*[a-zA-Z0-9])/'],
                    'message' => 'Address line 2 contains invalid characters',
                ],
            ]);
    }

    public function jwt($field, $salt = null, $className = null, $message = null, $when = null)
    {
        $extra = array_filter([
            'on'      => $when,
            'message' => $message ?? 'Provided value is not a valid token.',
        ]);

        return $this->add($field, 'validToken', $extra + [
                'rule'     => ['jwtToken', $salt, $className],
                'provider' => 'base',
            ]);
    }

    public function existsInTable($field, $tableName, $targetField = null, $message = null, $when = null)
    {
        $extra = array_filter([
            'on'      => $when,
            'message' => $message ?? 'Provided value is invalid.',
        ]);

        return $this->add($field, 'validValue', $extra + [
                'rule'     => ['recordsExistsInTable', $tableName, $targetField ?: $field],
                'provider' => 'base',
            ]);
    }

    /**
     * Table unique validator.
     *
     * @param string      $field     field of table that must have unique values
     * @param string      $tableName name of table
     * @param string|null $message   message will be displayed in error case
     *
     * @return $this
     */
    public function unique(string $field, string $tableName, $message = null)
    {
        return $this->add($field, 'unique', [
            'rule'     => ['recordsNotExistsInTable', $tableName, $field],
            'message'  => $message ?? 'Value already has taken',
            'provider' => 'base',
        ]);
    }

    /**
     * Check table table has no provided value
     *
     * @param string $check     Value to check
     * @param string $tableName Table alias
     * @param string $field     Field name
     *
     * @return bool
     */
    public static function recordsNotExistsInTable($check, $tableName, $field): bool
    {
        return !static::recordsExistsInTable($check, $tableName, $field);
    }

    /**
     * Validates a datetime value to match ISO 8601 standard
     *
     * All values matching the "date" core validation rule, and the "time" one will be valid
     *
     * @param string|\DateTimeInterface $check      Value to check
     * @param string|array              $dateFormat Format of the date part. See Validation::date() for more
     *                                              information.
     *
     * @return bool True if the value is valid, false otherwise
     * @see \Cake\Validation\Validation::date()
     * @see \Cake\Validation\Validation::time()
     */
    public static function iso8601dateTime($check, $dateFormat = 'ymd')
    {
        $pattern = '/^(\d{4}\-\d{2}\-\d{2})(?:T)(\d{2}:\d{2}:\d{2}).*$/';
        if (is_string($check) && preg_match($pattern, $check)) {
            $check = preg_replace(
                $pattern,
                '$1 $2',
                $check
            );
        }

        return Validation::datetime($check, $dateFormat);
    }

    /**
     * Validates if provided value is a valid IANA/Olson timezone
     *
     * @param string $check Value to check
     *
     * @return bool True if the value is valid, false otherwise
     * @see \DateTimeZone::listIdentifiers()
     */
    public static function ianaTimezone($check)
    {
        return in_array($check, \DateTimeZone::listIdentifiers());
    }

    /**
     * Validates if provided value is a valid JWT token (could be decoded with the salt provided or default one)
     *
     * @param string      $check     Value to check.
     * @param string|null $salt      Security salt for decoding a token.
     * @param string|null $className String representing class name of payload object for json mapper.
     *
     * @return bool True if the value is valid, false otherwise
     */
    public static function jwtToken($check, $salt = null, $className = null)
    {
        $helper = new JwtHelperService();

        return null !== $helper->decodeToken($check, $className, $salt);
    }

    /**
     * Validates if specific table has rows with the field equals to provided value
     *
     * @param string $check     Value to check
     * @param string $tableName Table alias
     * @param string $field     Field name
     *
     * @return bool True if the value is valid, false otherwise
     */
    public static function recordsExistsInTable($check, $tableName, $field)
    {
        return TableRegistry::get($tableName)->exists([$field => $check]);
    }

    /**
     * Validates if value is a valid Google's reCAPTCHA v2 response
     *
     * @param string $check Value to check
     *
     * @return bool True if the value is valid, false otherwise
     */
    public static function reCaptcha($check)
    {
        $environment = Configure::read('environment');
        if (Environment::TEST === $environment || Environment::LOCAL === $environment) {
            return true;
        }

        $recaptcha   = new ReCaptcha(Configure::read('ReCaptcha.secret'));
        $apiResponse = $recaptcha->verify($check);

        return $apiResponse->isSuccess();
    }
}
